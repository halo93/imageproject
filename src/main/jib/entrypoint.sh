#!/bin/sh

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
mkdir ~/.aws
cd ~/.aws
printf "[default]\nregion = us-west-1" > config
printf "[default]\naws_access_key_id = AKIAT67LMDPSOGCMNB4C\naws_secret_access_key = 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" > credentials
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.company.imageproject.ImageprojectApp"  "$@"
