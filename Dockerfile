# syntax=docker/dockerfile:1

# Builder stage: install JDK via Gradle image and setup Chrome, ChromeDriver, and Xvfb
FROM gradle:7.6-jdk17 AS builder
USER root

# Install OS deps, Chrome, ChromeDriver, and Xvfb for headless and headful tests
RUN apt-get update && \
    apt-get install -y wget gnupg2 apt-transport-https ca-certificates unzip curl xvfb && \
    # Download and install Chrome
    wget -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    dpkg -i /tmp/chrome.deb || apt-get -f install -y && \
    rm /tmp/chrome.deb && \
    # Download matching ChromeDriver
    CHROME_DRIVER_VERSION=$(curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -O /tmp/chromedriver.zip \
      https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /var/lib/apt/lists/* /tmp/chromedriver.zip

# Expose Chrome paths
ENV CHROME_BIN=/usr/bin/google-chrome
ENV CHROME_DRIVER=/usr/local/bin/chromedriver

# Copy project sources and set permissions
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN chmod +x gradlew

# Default: wrap test execution in xvfb-run to support both headless and headful tests
CMD ["bash", "-lc", "xvfb-run -s '-screen 0 1920x1080x24' ./gradlew clean testApi testWeb test --no-daemon"]