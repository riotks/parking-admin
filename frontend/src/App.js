import React from 'react';
import {Admin, Resource} from 'react-admin';
import {UserList} from './data-store/model/users';
import {PostList} from './data-store/model/posts';
import jsonServerProvider from 'ra-data-json-server';

const dataProvider = jsonServerProvider('http://jsonplaceholder.typicode.com');

const App = () => (
  <Admin dataProvider={dataProvider}>
    <Resource name="posts" list={PostList}/>
    <Resource name="users" list={UserList}/>
  </Admin>
);

/* X-Total-Count (TODO) header return for records.

const dataProvider = jsonServerProvider('/parking-api');
const App = () => (
  <Admin dataProvider={dataProvider}>
    <Resource name="records" list={ListGuesser}/>
  </Admin>
);

TODO CORS (use proxy in package.json:

  "proxy": "http://localhost:8069"
*/

export default App;