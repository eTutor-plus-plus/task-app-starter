/**
 * Reads the version from the pom.xml file.
 *
 * @param {string} contents The file contents.
 * @return {string} The version string.
 */
module.exports.readVersion = function (contents) {
    const {XMLParser} = require("fast-xml-parser");
    const parser = new XMLParser();
    const xml = parser.parse(contents);
    return xml.project.version;
};

/**
 * Writes the version to the provided file contents.
 *
 * @param {string} contents The file contents.
 * @param {string} version The version to write.
 * @return {string} The modified file contents.
 */
module.exports.writeVersion = function (contents, version) {
    // make sure to not change version of dependency (assumes version is specified in top of file).
    const upperPart = contents.substring(0, 1000);
    const lowerPart = contents.substring(1000);

    const oldVersion = this.readVersion(contents);
    return upperPart.replace(`<version>${oldVersion}</version>`, `<version>${version}</version>`) + lowerPart;
};
