.PHONY: install dev-backend dev-frontend build-all test-all

install:
	pnpm install
	cd apps/api && ./mvnw install -DskipTests

dev-backend:
	cd apps/api && ./mvnw spring-boot:run

dev-frontend:
	pnpm nx serve storefront

build-all:
	cd apps/api && ./mvnw clean package -DskipTests
	pnpm nx run-many -t build

test-all:
	cd apps/api && ./mvnw test
	pnpm nx run-many -t test
