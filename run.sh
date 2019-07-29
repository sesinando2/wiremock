#!/bin/bash

FILES="$(pwd)/src/test/resources/__files/"
MAPPINGS="$(pwd)/src/test/resources/mappings/"
# docker run -it -v $FILES:/opt/__files/ -v $MAPPINGS:/opt/mappings/ wiremock /bin/bash
docker run -p 8888:8888 -v $FILES:/opt/__files/ -v $MAPPINGS:/opt/mappings/ wiremock