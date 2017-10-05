@Grapes(
    @Grab(group='org.jsoup', module='jsoup', version='1.10.3')
)


def platforms = [
	jdk: [
		linux: [
			arm32: 'arm32-vfp-hflt',
			arm64: 'arm64-vfp-hflt',
			x86: 'i586',
			x64: 'x64'
		]
	],
	jre: [
		windows: [
			x86: 'i586',
			x64: 'x64'
		],
		macosx: [
			x64: 'x64'
		]
	]
]

// parse version/update/build from release string
def name    = properties.product
def release = properties.release =~ /(?<major>\d+)[+](?<build>\d+)/
def version = release[0][1]
def build = release[0][2]


// package repository
def site = "http://download.oracle.com/otn-pub/java/jdk/${version}+${build}"

// grep SHA-256 checksums from Oracle
def digest = org.jsoup.Jsoup.connect("https://www.oracle.com/webfolder/s/digest/${version}checksum.html").get()

// generate properties file
ant.propertyfile(file: 'build-jdk.properties', comment: "${name} ${version} binaries") {
	entry(key:"jdk.name", value: name)
	entry(key:"jdk.version", value: version)

	platforms.each{ type, o ->
		o.each{ os, m ->
			m.each{ arch, pkg ->
				def filename = "${type}-${version}_${os}-${pkg}_bin.tar.gz"
				def url = "${site}/${filename}"

				def checksum = digest.select('tr')
								.find{ it.select('td').any{ it.text() == filename } }
								.findResult{ it.select('td').findResult{ it.text().find( /sha256: (\p{XDigit}{64})/ ){ match, checksum -> checksum.toLowerCase() } } }

				entry(key:"${type}.${os}.${arch}.url", value: url)
				entry(key:"${type}.${os}.${arch}.sha256", value: checksum)
			}
		}
	}
}
