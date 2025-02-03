
#!/bin/sh

download_release() {
  # Set the GitHub repository owner and name (passed as argument)
  REPO="$1"

  # Specify the directory to save the downloaded file
  DOWNLOAD_DIR="$2"
  if [ -z "$DOWNLOAD_DIR" ]; then
    DOWNLOAD_DIR="./downloads"  # Default directory if not provided
  fi

  # Create the download directory if it doesn't exist
  mkdir -p "$DOWNLOAD_DIR"

  # Detect the system architecture (e.g., x86_64, aarch64)
  ARCH=$(uname -m)
  OS=$(uname -s)

  # Map OS names to release names
  if [ "$OS" = "Linux" ]; then
    SYSTEM="linux"
  elif [ "$OS" = "Darwin" ]; then
    SYSTEM="darwin"
  elif [ "$OS" = "WindowsNT" ]; then
    SYSTEM="windows"
  else
    echo "Unsupported OS: $OS"
    exit 1
  fi

  # Map architecture names to release names
  if [ "$ARCH" = "x86_64" ]; then
    ARCHITECTURE="amd64"
  elif [ "$ARCH" = "aarch64" ]; then
    ARCHITECTURE="arm64"
  elif [ "$ARCH" = "armv7l" ]; then
    ARCHITECTURE="arm"
  else
    echo "Unsupported architecture: $ARCH"
    exit 1
  fi

  # Get the latest release information from GitHub API (JSON response)
  latest_release=$(curl -s "https://api.github.com/repos/$REPO/releases/latest")
  
  # Clean control characters (ASCII 0-31)
  latest_release=$(echo "$latest_release" | tr -d '\000-\031')

  # Find the download URL for the release based on system and architecture
  download_url=$(echo "$latest_release" | grep -oP '"browser_download_url":\s*"\K[^"]+' | grep -i "$SYSTEM" | grep -i "$ARCHITECTURE" | head -n 1)

  # Check if the download URL was found
  if [ -z "$download_url" ]; then
    echo "No suitable release found for $SYSTEM $ARCHITECTURE."
    exit 1
  fi

  # Extract the file name from the URL for saving the binary
  binary_name=$(basename "$download_url")

  # Download the binary into the specified directory
  echo "Downloading from: $download_url"
  curl -L -o "$DOWNLOAD_DIR/$DOWNLOAD_DIR" "$download_url"

  echo "Download complete. The binary is saved as '$DOWNLOAD_DIR/$DOWNLOAD_DIR'."
}

# Example of how to call the function
# First argument is the repository, second is the directory where the file will be downloaded

if [ ! -d "game-controller" ]; then
  download_release "RoboCup-SSL/ssl-game-controller" "game-controller"
else
  echo "game-controller already exists."
fi

if [ ! -d "vision-client" ]; then
  download_release "RoboCup-SSL/ssl-vision-client" "vision-client"
else
  echo "vision-client already exists."
fi
