#!/usr/bin/env bash
set -euo pipefail
cd /workspace

health_url="http://127.0.0.1:8080/actuator/health"
if curl -sf "${health_url}" >/dev/null 2>&1; then
  exit 0
fi

nohup ./mvnw -q spring-boot:run > /tmp/spring-boot.log 2>&1 &
pid=$!
trap 'kill "${pid}" 2>/dev/null || true' EXIT

for _ in $(seq 1 90); do
  if curl -sf "${health_url}" >/dev/null 2>&1; then
    trap - EXIT
    exit 0
  fi
  if ! kill -0 "${pid}" 2>/dev/null; then
    echo "Spring Boot exited before becoming healthy; log:" >&2
    tail -n 40 /tmp/spring-boot.log >&2 || true
    exit 1
  fi
  sleep 2
done

echo "Timed out waiting for ${health_url}" >&2
tail -n 40 /tmp/spring-boot.log >&2 || true
exit 1
