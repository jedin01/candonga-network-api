#!/usr/bin/env bash
set -e

echo "Running composer"
composer install --no-dev --working-dir=/var/www/html --no-interaction --optimize-autoloader

echo "Caching config..."
php artisan config:cache

echo "Caching routes..."
php artisan route:cache

echo "Running migrations..."
php artisan migrate --force

# (opcional) criar link storage
php artisan storage:link || true

# start da stack (ex.: nginx + php-fpm via supervisord) - dep depende do seu Dockerfile
# se seu Dockerfile iniciar nginx/php-fpm automaticamente, não precisa deste comando aqui.
exec "$@"
