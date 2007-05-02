package org.jboss.aspects.tx;

import javax.transaction.TransactionManager;

/**
 * When we don't know where we are
 */
class NullTransactionTimeoutConfigurationReader implements TxTimeoutReader
{
   public int getTransactionTimeOut(TransactionManager tm)
   {
      return 0;
   }
}