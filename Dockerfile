# syntax=docker/dockerfile:1

# Use Gradle image with JDK 17
FROM gradle:7.6-jdk17 AS builder

USER root

# Install Chrome dependencies and Chrome itself
RUN apt-get update && \
    apt-get install -y wget gnupg unzip curl && \
    wget -qO - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
         > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    # Install matching ChromeDriver
    CHROME_DRIVER_VERSION=$(curl -s https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -O /tmp/chromedriver.zip \
      https://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /var/lib/apt/lists/* /tmp/chromedriver.zip

# Set environment variables for Chrome
ENV CHROME_BIN=/usr/bin/google-chrome
ENV CHROME_DRIVER=/usr/local/bin/chromedriver

# Set working directory and copy project sources
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

# Ensure the Gradle wrapper is executable
RUN chmod +x gradlew

# Default command: clean and run all test suites
CMD ["./gradlew", "clean", "testApi", "testWeb", "test", "--no-daemon"]