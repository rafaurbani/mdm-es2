const Eureka = require('eureka-js-client').Eureka;

// Configure Eureka client
const client = new Eureka({
    instance: {
        app: 'mdm-microservice',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        port: {
            '$': 3000,
            '@enabled': 'true',
        },
        vipAddress: 'mdm-microservice',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/'
    }
});
// Start Eureka client
client.start(error => {
    console.log('Eureka client started with error:', error);
});