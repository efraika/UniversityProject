FROM ubuntu:xenial

SHELL [ "/bin/bash", "-eux", "-o", "pipefail", "-c" ]

RUN \
        apt update \
    &&  apt install -y --no-install-recommends \
                git \
                curl \
                gcc \
                flex \
                bison \
                libsdl2-dev \
                libsdl2-image-dev \
                libreadline6-dev \
                libjpeg8-dev \
                libpng12-dev \
                python3-venv \
                check \
                file

SHELL [ "/bin/sh", "-c" ]