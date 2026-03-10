#!/bin/bash

WORKING_DIR=/opt/spring/isms
ARTIFACT_DIR=/home/ubuntu/isms
ARTIFACT_NAME=oshea-spring-app-1.0.0-SNAPSHOT.jar
JAVA_SERVICE=oshea-spring.service
# Specify the file name
# Get the current date in the format YYYY-MM-DD
current_date=$(date "+%Y-%m-%d")

# Extract the file extension (if any)
file_extension="${ARTIFACT_NAME##*.}"

# Remove the extension from the original filename (if any)
filename_no_ext="${ARTIFACT_NAME%.*}"

# Append the current date to the file name with .bak  extension
new_filename="${filename_no_ext}_${current_date}.${file_extension}.bak"


echo "[Kill] Port 9092"
fuser -k 9092/tcp

if [[ ! -d $WORKING_DIR  ]]; then
  sudo mkdir $WORKING_DIR
  sudo chown ubuntu:ubuntu $WORKING_DIR
fi

if [ -f "$WORKING_SIR/oshea-spring-app-1.0.0-SNAPSHOT.jar" ]; then
    echo "[BACKING UP] Existing jar files"
    cd $WORKING_DIR
    sudo rm *.bak
    sudo mv "$ARTIFACT_NAME" "$new_filename"
fi




cd $ARTIFACT_DIR/target
echo "[COPY] Build file to /opt/spring/isms"
sudo mv $ARTIFACT_NAME $WORKING_DIR
sudo chown -R ubuntu:ubuntu $WORKING_DIR
chmod +x $WORKING_DIR/$ARTIFACT_NAME

echo "[RUN] Running the service"
sudo systemctl restart $JAVA_SERVICE

exit 0