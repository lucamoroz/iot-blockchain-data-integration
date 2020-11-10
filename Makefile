start:
	mvn -f dataFilter/pom.xml package && docker-compose build && docker-compose up -d

restart:
	docker-compose restart

build:
	docker-compose build

purge:
	docker-compose down -v --rmi all --remove-orphans
