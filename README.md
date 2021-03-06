# noELLE
Collection of utilities for developing plugins.

![Percentage of issues still open](https://img.shields.io/github/issues/iwmc-git/noELLE?style=for-the-badge)
[![GitHub forks](https://img.shields.io/github/forks/iwmc-git/noELLE?style=for-the-badge)](https://github.com/iwmc-git/noELLE/network)
[![GitHub stars](https://img.shields.io/github/stars/iwmc-git/noELLE?style=for-the-badge)](https://github.com/iwmc-git/noELLE/stargazers)
[![GitHub license](https://img.shields.io/github/license/iwmc-git/noELLE?style=for-the-badge)](https://github.com/iwmc-git/noELLE/blob/master/LICENSE)   

## Information
- noELLE designed only for Velocity and Paper - means it won't work on other platforms.
- noELLE runs **ONLY** on Java version **17**.
- Documentation for the project will be made when the project itself is completed

## Overlay (what?)
The following modules are currently implemented:
- Simple configuration API (Standalone)
- Simple database API (Standalone)
- Encryptor API (Standalone)
- Async scheduler API (Standalone)
- Other utils, e.g update checker, file utils.. (Standalone)
- Languages API (Velocity, Paper)
- Simple items API (Paper)
- Simple and paged inventories API (Paper)

## Building
To start building the project, you must enter this command in the terminal:
`./gradlew shadowJar`
> Please note that you need Java version 17 to build, and you must also be in the root directory of the project

## Get this
All builds located [here](https://build.iwmc.pw/job/noELLE/).

## Compatibility
- To use the library on Velocity, the recommended version is at least 3.1.2 (The build doesn't matter).
- To use the library on Paper, the recommended version is at least 1.18.2 (build 386).