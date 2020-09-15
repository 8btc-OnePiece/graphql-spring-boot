function introspectionProvider(introspectionQuery) {
        var path = document.getElementById("graphqlEndpoint").value;
        return fetch(path, {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ query: introspectionQuery }),
            credentials: 'include'
        }).then(function(response) {
            return response.text();
        }).then(function(responseBody) {
            try {
                return JSON.parse(responseBody);
            } catch(error) {
                return responseBody;
            }
        });
    }

    // Render <Voyager /> into the body.
    GraphQLVoyager.init(document.getElementById('voyager'), {
        introspection: introspectionProvider
    })