# Implementing an Automated Teller Machine (ATM) System
ATM System Handels Concurrent Transactions
<hr/>
### Relational Database Backend
As a first step, deploy a relational database to the cloud. As shown in the exercise, we recommend to deploy a MySQL database to Amazon Web Services (AWS) by using its Relational Database Service (RDS).
However, you are free to choose another provider or database implementation.
Create a simple and sensible database scheme that allows you to keep track of account balances.

### ATM Java Client
Develop a Java ATM client that provides a command line interface (cli) offering the following common ATM functionality to the user: 
A customer must be able to…
  <li>login using her account number and corresponding pin code.</li>
  <li>deposit and withdraw funds.</li>
  <li>display her account balance.</li>
Make sure that the operation’s results adequately reflect in the database.

### Concurrent Access
In the real world, many ATM transactions happen in parallel. Under all circumstances, our system needs to ensure key invariants globally – even in case of concurrent transactions.
In our case, this means that the account balance must not be negative! This constraint must hold even in case of concurrent transaction. 
Please assume that multiple ATMs (instances of the Java client) exist and customers may have access to the same account such that they can withdraw money
simultaneously from different ATMs.
  <li>Conceptually discuss how such an invariant could be guaranteed. Should enforcement happen in the database on within clients?</li>
  <li>Implement one of the solutions you discussed in 1 within your system. Test your implementation in a sensible way and document your testing procedure and results.</li>
