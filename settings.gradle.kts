rootProject.name = "noELLE"

include(
    "noelle-libraries:libraries-api",
    "noelle-libraries:libraries-impl"
)

include(
    "noelle-standalone:standalone-configuration:common-configuration",
    "noelle-standalone:standalone-configuration:hocon-configuration",
    "noelle-standalone:standalone-configuration:yaml-configuration",

    "noelle-standalone:standalone-database:common-database",
    "noelle-standalone:standalone-database:h2-database",
    "noelle-standalone:standalone-database:mariadb-database",

    "noelle-standalone:standalone-encryptor",
    "noelle-standalone:standalone-scheduler",
    "noelle-standalone:standalone-utils"
)

include(
    "noelle-loaders:loaders-paper",
    "noelle-loaders:loaders-velocity",
    "noelle-loaders:loaders-common"
)

include(
    "noelle-common:common-languages"
)

include(
    "noelle-paper:paper-languages"
)

include(
    "noelle-velocity:velocity-languages"
)