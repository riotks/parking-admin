import React from 'react';
// import {List, Datagrid, TextField, DateField, ReferenceField} from 'react-admin';
import {List, Datagrid, TextField, DateField} from 'react-admin';

export const RecordList = props => (
  <List {...props}>
    <Datagrid rowClick="edit">
      <TextField source="id" />
      <TextField source="customerId" />
      {/*<ReferenceField source="customerId" reference="customers"><TextField source="id" /></ReferenceField>*/}
      <TextField source="customerType" />
      <TextField source="status" />
      <DateField source="beginAt" locales="et-ET" showTime />
      <DateField source="endBy" locales="et-ET" showTime />
    </Datagrid>
  </List>
);