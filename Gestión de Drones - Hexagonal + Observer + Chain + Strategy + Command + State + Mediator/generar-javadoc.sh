#!/usr/bin/env sh
set -eu
mvn javadoc:javadoc
echo "Javadoc generado en target/site/apidocs/index.html"
