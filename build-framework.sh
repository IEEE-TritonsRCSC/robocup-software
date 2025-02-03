#!/bin/sh

# Define the folder name and the Git repository URL
FOLDER="framework"
REPO_URL="https://github.com/robotics-erlangen/framework.git"

# Check if the folder exists
if [ ! -d "$FOLDER" ]; then
  echo "Folder '$FOLDER' does not exist. Cloning repository..."
  git clone "$REPO_URL" "$FOLDER"
  if [ $? -eq 0 ]; then
    echo "Repository cloned successfully into '$FOLDER'."
  else
    echo "Failed to clone repository."
    exit 1
  fi
else
  echo "Folder '$FOLDER' already exists."
fi

cd framework
if [ ! -d "build" ]; then
    echo "Folder 'build' does not exist. Creating build..."
    mkdir build
fi
cd build

if [ ! -f "CMakeCache.txt" ]; then
  echo "CMake has not been run yet. Running cmake .."
  cmake .. 
  if [ $? -ne 0 ]; then
    echo "cmake .. failed. Exiting."
    exit 1  # Exit the script if cmake fails
  fi               # Run cmake to configure the project
else
  echo "CMakeCache.txt exists, cmake has already been run."
fi


SIMULATOR_CLI="bin/simulator-cli"

if [ ! -f "$SIMULATOR_CLI" ]; then
  echo "$SIMULATOR_CLI does not exist. Running make simulator-cli to build it..."
  make -i simulator-cli
else
  echo "$SIMULATOR_CLI already exists."
fi

cd ../..
