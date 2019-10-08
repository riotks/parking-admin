import React from 'react';
// import {List, Datagrid, TextField, DateField, ReferenceField} from 'react-admin';
import {List, Datagrid, TextField, NumberField, DateField} from 'react-admin';

export const InvoiceList = props => (
  <List {...props}>
    <Datagrid rowClick="edit">
      <TextField source="id" />
      <TextField source="customerId" />
      {/*<ReferenceField source="customerId" reference="customers"><TextField source="id" /></ReferenceField>*/}
      <NumberField source="amount" />
      <TextField source="currency" />
      <DateField source="created_at" locales="et-ET" showTime />
    </Datagrid>
  </List>
);