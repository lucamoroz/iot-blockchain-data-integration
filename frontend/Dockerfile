FROM tiangolo/node-frontend:10 as build-stage

WORKDIR /app

COPY package*.json /app/
COPY patch.js /app/

RUN npm install
RUN npm run postinstall

ARG configuration=production
COPY ./ /app/

RUN npm run build -- --output-path=./dist/out --configuration $configuration

FROM nginx:1.15

COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html

COPY --from=build-stage /nginx.conf /etc/nginx/conf.d/default.conf
