#!/usr/bin/env bash
set -euo pipefail
cd /workspace
chmod +x ./mvnw
./mvnw -B -q -DskipTests package
