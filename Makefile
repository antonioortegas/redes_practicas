# Makefile

# Define directories and files
P7_JAR = src/practica7_SMTP/fakeSMTP-2.0.jar
VIDEO_SCRIPT = src/practica6_HTTP/video/script.py

# Default target
.DEFAULT_GOAL := help

# Phony targets
.PHONY: jar p7 p6 video help

# Target to run the jar file fakeSMTP
jar: $(P7_JAR)
	java -jar $(P7_JAR)

# Target to run the script.py
video: $(VIDEO_SCRIPT)
	python $(VIDEO_SCRIPT)

# Help target to display usage information
help:
	@echo "Usage:"
	@echo "  make jar    - Run the jar file fakeSMTP"
	@echo "  make video  - Run the script.py"
	@echo "  make help   - Display this help message"