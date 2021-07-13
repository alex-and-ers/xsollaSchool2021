FROM alpine:3.8 as liberica

ARG GLIBC_PREFIX=/usr/glibc
ARG EXT_GCC_LIBS_URL=https://archive.archlinux.org/packages/g/gcc-libs/gcc-libs-8.3.0-1-x86_64.pkg.tar.xz
ARG EXT_ZLIB_URL=https://archive.archlinux.org/packages/z/zlib/zlib-1%3A1.2.11-3-x86_64.pkg.tar.xz
ARG LANG=en_US.UTF-8

ENV  LANG=${LANG} \
     LANGUAGE=${LANG}:en

ARG LIBERICA_ROOT=/usr/lib/jvm/jdk-13-bellsoft
ARG LIBERICA_VERSION=13
ARG LIBERICA_VARIANT=jdk
ARG LIBERICA_USE_LITE=1
WORKDIR /usr/local/app
ADD xsollaSchoolWebapp-1.0-SNAPSHOT.jar app.jar
CMD java -jar ./app.jar
