// OpenJDK for x64 Windows / Linux / Mac
def openjdk = [
	[os: 'windows', arch: 'x64', pkg: 'windows-x64_bin.zip'],
	[os: 'mac',     arch: 'x64', pkg: 'osx-x64_bin.tar.gz'],
	[os: 'linux',   arch: 'x64', pkg: 'linux-x64_bin.tar.gz'],
	[os: 'linux',   arch: 'aarch64', pkg: 'linux-aarch64_bin.tar.gz']
]

// AdoptOpenJDK for jdk
def adoptopenjdk = [
	[type: 'jdk', os: 'linux',   arch: 'arm',  pkg: 'tar.gz']
]
// BellSoft Liberica JDK for embedded devices
def liberica = [
	[type: 'jdk', os: 'linux',   arch: 'armv7l',  pkg: 'linux-arm32-vfp-hflt.tar.gz'],
	[type: 'jdk', os: 'linux',   arch: 'ppc64le', pkg: 'linux-ppc64le.tar.gz'],
	[type: 'jdk', os: 'linux',   arch: 'x86',     pkg: 'linux-i586.tar.gz'],
	[type: 'jdk', os: 'windows', arch: 'x86',     pkg: 'windows-i586.zip'],
]


// Gluon JavaFX
def javafx = [
	[os: 'windows', arch: 'x64', pkg: 'windows-x64_bin-jmods.zip'],
	[os: 'mac',     arch: 'x64', pkg: 'osx-x64_bin-jmods.zip'],
	[os: 'linux',   arch: 'x64', pkg: 'linux-x64_bin-jmods.zip']
]


// General Build Properties
def name = properties.product
//def version = properties.release

def (version, build) = properties.release.tokenize(/[+]/)
def (major, minor, update) = version.tokenize(/[.]/)
def uuid = properties.uuid

def sha256(url) {
	try {
		return new URL("${url}.sha256").text.tokenize().first()
	} catch(e) {
		println e
	}

	try {
		return new URL("${url}.sha256.txt").text.tokenize().first()
	} catch(e) {
		println e
	}

	def file = new File('cache', url.tokenize('/').last())
	new AntBuilder().get(src: url, dest: file, skipExisting: 'yes')
	return file.bytes.digest('SHA-256').padLeft(64, '0')
}


// generate properties file
ant.propertyfile(file: 'build-jdk.properties', comment: "${name} ${version} binaries") {
	entry(key: 'jdk.name', value: name)
	entry(key: 'jdk.version', value: version)

https://github.com/AdoptOpenJDK/openjdknull-binaries/releases/download/jdk-11.0.11+null/OpenJDKnullU-jdk_arm_linux_hotspot_11.0.11_null.tar.gz.sha256
https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_arm_linux_hotspot_11.0.11_9.tar.gz
adoptopenjdk.each{ jdk ->
		jdk.with {
			def url = "https://github.com/AdoptOpenJDK/openjdk${major}-binaries/releases/download/jdk-${version}+${build}/OpenJDK${major}U-${type}_${arch}_${os}_hotspot_${version}_${build}.${pkg}"
			def checksum = sha256(url)

			entry(key:"jdk.${os}.${arch}.url", value: url)
			entry(key:"jdk.${os}.${arch}.sha256", value: checksum)
		}


	}
}
