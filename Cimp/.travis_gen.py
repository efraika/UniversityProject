#!/usr/bin/python3

## This is a python script file to generate a .travis.yml file according to some contents.
## To update the .travis.yml file, you just have to run python3 .travis_gen.py > .travis.yml

LANG = "C"
DIST = "xenial"

J_BASE = "Base"
J_LINT = "Lint"
J_TEST = "Test"
JOBS = [J_BASE, J_LINT, J_TEST]

# There are 3 stages,
#   - Base tests the global tests, and do some tests to prevent human errors when filling files.mk for example.
#       Linters needs files.mk to have the right filenames.
#   - Lint tests running some external tools according to filenames.
#   - Test runs unit tests.

# Base is a stage that test the following things
#  - it tests if the .travis.yml file was modified directly without modifying this script.
#       We want here to modify this script instead of the .travis.yml file
#  - it tests if the project compile
#  - it tests if no c file is missing in Makefile (*.c in src)
#  - it tests if no header file is missing in Makefile (*.h in inc)

BASE_TASKS = [
    (J_BASE, "verifying travis.yml file", None, ["python3 .travis_gen.py > expected.out", "diff .travis.yml expected.out"]),
    (J_BASE, "test project compile", (None, True), ["make"]),
    (J_BASE, "all C files in Makefile", None, ["diff <(make print-SRC | tr ' ' '\\n' | sort) <(find src -name \"*.c\" | sort)"]),
    (J_BASE, "all headers files in Makefile", None, ["diff <(make print-INC | tr ' ' '\\n' | sort) <(find inc -name \"*.h\" | sort)"]),
]

# Lint is a stage that test the project using some external tools
#  - uncrustify, checks if the code respects the norm definned in the UNCRUSTIFY.cfg file
#  - cpplint, cppcheck, clang-tidy, infer, checks leaks, known errors and undefined behaviours

LINT_TASKS = [
    (J_LINT, "uncrustify", None, ["make uncrustify_check"]),
    (J_LINT, "cpplint", (["python3-venv"], False), ["make cpplint_run"]),
    (J_LINT, "cppcheck", (["cppcheck"], False), ["make cppcheck_run"]),
    (J_LINT, "clang-tidy", (["clang-tidy-6.0", "clang-6.0"], True), ["make clang_tidy_run"]),
    (J_LINT, "infer", (None, True), ["sudo make /usr/local/bin/infer", "make infer_run"]),
]

# Test is a stage that test if the project runs the unit tests, used by libcheck

TEST_TASKS = [
    (J_TEST, "libcheck", (["python3-venv", "check"], True), ["make images","make cimp_check"]),
]

INSTALL_APT_PREFIX = "sudo apt-get install -y --no-install-recommends"
INSTALL_SDL = ["libsdl2-dev", "libsdl2-image-dev"]

TASKS = BASE_TASKS + LINT_TASKS + TEST_TASKS

def print_header():
    print("language:", LANG)
    print("dist:", DIST)
    print()
    print("stages:")
    for job in JOBS:
        print("  -", job)
    print()

def build_install_apt(li_apt_deps, b_install_sdl):
    li = []
    if li_apt_deps is not None:
        li.extend(li_apt_deps)
    if b_install_sdl:
        li.extend(INSTALL_SDL)
    return (INSTALL_APT_PREFIX + " " + " ".join(li))

def print_dep(dep):
    (apt_deps, b_install_sdl) = dep
    install_apt_cmd = build_install_apt(apt_deps, b_install_sdl)
    print("      before_install: sudo apt-get update")
    print("      install:", install_apt_cmd)

def print_task(task):
    (stage, name, dep, cmds) = task
    print("    - name:", name)
    print("      stage:", stage)
    if dep is not None:
        print_dep(dep)
    print("      script:")
    for cmd in cmds:
        print("        -", cmd)

def main():
    print_header()
    print("matrix:")
    print("  include:")
    for task in TASKS:
        print_task(task)

if __name__ == "__main__":
    main()
