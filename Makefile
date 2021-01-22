start:
	docker-compose build && docker-compose --env-file .env.dev up -d

start-logs:
	docker-compose build && docker-compose --env-file .env.dev up

stop:
	docker-compose down

purge:
	docker-compose down -v --rmi all --remove-orphans
