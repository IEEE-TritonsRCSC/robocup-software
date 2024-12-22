#!/bin/sh

chmod +x ./game-controller/game-controller
chmod +x ./vision-client/vision-client

# Check if the symbolic link already exists
if [ ! -L /usr/local/bin/protoc ]; then
  # If not, create the symbolic link
  sudo ln -s $(which protoc) /usr/local/bin/protoc
  echo "Symbolic link created: /usr/local/bin/protoc -> $(which protoc)"
else
  echo "Symbolic link already exists at /usr/local/bin/protoc"
fi

cd software/tritonsoccerai
bash build-ai.sh
cd ../..
