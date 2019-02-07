# probableCausesBackend
Backend of Part IB Group Project Probable Causes

Last modified: 7th Feb 2019

Instead of implementing backend in an external server, I chose to use localhost at the moment for easier testing and debugging.
We can just use Cambridge domain provided for free if we want to put this online.

None of non-sample APIs are working at the moment.

## API

Return of any BQL commands will be a json object,
where the object is a set of tables
and a table is a set of rows
and a row is a set of mappings from a column name to a value.

API names can be shortened if they seem too wordy.

Since the API names can be changed, check the latest modified date above

None BQL APIs can be built on request.

### BQL Commands - Transactions

Transactions are groups of changes to a database that happen all at once or not at all. Transactions do not nest.

This has not implemented in BQL yet.

### BQL Commands - Data Definition Language

### BQL Commands - Meta-Modeling Language MML

#### CREATE, Populations

'''
/bql/command/create/default/{table}/{schema}
'''

#### DROP, Populations

'''
/bql/command/drop/default/{pop}
'''

#### ALTER, Populations

'''
/bql/command/alter/default/{pop}
'''

#### GUESS, Populations

'''
/bql/command/guess/default/{table}
'''

### BQL Queries

#### SELECT

'''
/bql/query/select/default/{columns}
'''

'''
/bql/query/select/from/{columns}/{table}
'''

#### ESTIMATE

'''
/bql/query/estimate/{mode='by'|'from'|'from-variables-of'|'from-pairwise-variables-of'}/{expression}/{population}
'''

#### INFER

'''
/bql/query/infer/default/{columnNames}/{population}
'''

'''
/bql/query/infer/explicit/{expression}/{population}
'''

#### SIMULATE

'''
/bql/query/simulate/default/{columnNames}/{population}
'''

### BQL Expressions - Model Estimators

#### PREDICTIVE PROBABILITY

'''
/bql/expression/modelEstimator/predictiveProbability/default/{column}
'''

#### PROBABILITY DENSITY

'''
/bql/expression/modelEstimator/probabilityDensity/target/{column}/{value}
'''

'''
/bql/expression/modelEstimator/probabilityDensity/targets/{targets}
'''

'''
/bql/expression/modelEstimator/probabilityDensity/value/{value}
'''

#### SIMILARITY

'''
/bql/expression/modelEstimator/similarity/default/{column}
'''

#### PREDICTIVE RELEVANCE

'''
/bql/expression/modelEstimator/predictiveRelevance/existingRows/{booleanExpression}/{column}
'''

'''
/bql/expression/modelEstimator/predictiveRelevance/hypotheticalRows/{booleanExpression}/{column}
'''

'''
/bql/expression/modelEstimator/predictiveRelevance/existingRowsAndHypotheticalRows/{booleanExpression1}/{booleanExpression2}/{column}
'''

#### CORRELATION

'''
/bql/expression/modelEstimator/correlation/default/{column}
'''

#### DEPENDENCE PROBABILITY

'''
/bql/expression/modelEstimator/dependenceProbability
'''

#### MUTUAL INFORMATION

'''
/bql/expression/modelEstimator/mutualInformation
'''

### BQL Expressions - Model Predictions

#### PREDICT

'''
/bql/expression/modelPrediction/predict/default/{column}
'''

### Other

Sample API - simple CRUD
This is sample API to test and give an example of how Java Spring passes APIs to ReactJS.
This is NOT related to our project.
All data is returned as JSON

GET
/groups
* returns all group objects
/group/{id}
* returns a group with given id

POST
/group
* creates a new group

UPDATE
/group
* updates an existing group

DELETE
/group/{id}
* deletes a group with given id
