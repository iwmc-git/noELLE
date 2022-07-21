rootProject.name = "noELLE"

include(
    "noelle-standalone:standalone-configuration:common-configuration",
    "noelle-standalone:standalone-configuration:hocon-configuration",
    "noelle-standalone:standalone-configuration:yaml-configuration",
    "noelle-standalone:standalone-configuration:json-configuration",

    "noelle-standalone:standalone-database:common-database",
    "noelle-standalone:standalone-database:h2-database",
    "noelle-standalone:standalone-database:mariadb-database",

    "noelle-standalone:standalone-encryptor",
    "noelle-standalone:standalone-scheduler",
    "noelle-standalone:standalone-utils"
)

include(
    "noelle-common:common-languages",
    "noelle-common:common-messages",
    "noelle-common:common-utils"
)

include(
    "noelle-paper:paper-languages",
    "noelle-paper:paper-items",
    "noelle-paper:paper-inventories",
    "noelle-paper:paper-messages"
)

include(
    "noelle-velocity:velocity-languages",
    "noelle-velocity:velocity-messages"
)

include(
    "noelle-loaders:loaders-paper",
    "noelle-loaders:loaders-velocity",
    "noelle-loaders:loaders-common"
)