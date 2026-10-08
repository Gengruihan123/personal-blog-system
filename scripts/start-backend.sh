#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")/../backend"
command -v mvn >/dev/null || { echo "Please install Maven and JDK 17 first." >&2; exit 1; }
exec mvn spring-boot:run
