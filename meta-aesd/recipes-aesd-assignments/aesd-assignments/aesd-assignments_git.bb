# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-paulnwoko.git;protocol=ssh;branch=master"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo.
SRCREV = "b615405cd5762bc1f2bb1007e99b876c5a7ec1f7"
#uncomment next line to make it pull latest commit automatically.
#SRCREV = "${AUTOREV}"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
# Integration for the init script
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop.sh"
INITSCRIPT_PARAMS:${PN} = "defaults 99 20"

# Packaging: Tell Yocto which files belong to this package
FILES:${PN} += "${bindir}/aesdsocket"
FILES:${PN} += "${sysconfdir}/init.d/aesdsocket-start-stop.sh"

# TODO: customize these as necessary for any libraries you need for your application
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile () {
# Force use of cross-compiler
#	oe_runmake CC="${CC}" LDFLAGS="${LDFLAGS}"
	oe_runmake CC="${CC}" LDFLAGS="${LDFLAGS}" CFLAGS="${CFLAGS}"
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb

	# Create the /usr/bin directory in the target rootfs
    install -d ${D}${bindir}
	
	# Install the executable (compiled in do_compile) to /usr/bin
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/

	# Create the /etc/init.d directory in the target rootfs
	install -d ${D}${sysconfdir}/init.d
	# install start script
	install -m 0755 ${S}/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d/
}
