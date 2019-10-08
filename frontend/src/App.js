import React from 'react';
import {Admin, Resource} from 'react-admin';
import {InvoiceList} from './data-store/model/invoices';
import {RecordList} from './data-store/model/records';
import jsonServerProvider from 'ra-data-json-server';

const dataProvider = jsonServerProvider('/parking-api');
const App = () => (
  <Admin dataProvider={dataProvider}>
    <Resource name="records" list={RecordList}/>
    <Resource name="invoices" list={InvoiceList} />
  </Admin>
);

export default App;