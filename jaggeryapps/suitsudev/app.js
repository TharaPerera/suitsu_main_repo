
var caramel = require('caramel');

caramel.configs({
    context: '/htmlBlogAggregator',
    cache: true,
    negotiation: true,
    themer: function () {
        return 'htmlBlogAggregator';
    }
});


var configs = require('/config.json');


var server = require('/modules/server.js');
server.init(configs);

var user = require('/modules/user.js');
user.init(configs);
