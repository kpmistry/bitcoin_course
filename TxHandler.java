import java.util.*;

public class TxHandler {


	private UTXOPool ledger;
	
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        ledger = new UTXOPool(utxoPool);
    }

	public TxHandler(){ // if no arguemnts passed
        ledger = new UTXOPool();
    }
			

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
		
		if(!(checkAllOutputsClaimed(tx))){
			return false;
		}
		if(!(checkAllSignatures(tx))){
				return false;
		}		
		if(!(checkForDuplicateUTXO(tx))){
				return false;
		}
		return true;
	}
	
	public boolean checkAllOutputsClaimed(Transaction tx) {
			UTXO temputxo;	

			for(int i=0; i<tx.numInputs();i++) {
				Transaction.Input in = tx.getInput(i);
				temputxo = new UTXO(in.prevTxHash, in.outputIndex);
				if (!( ledger.contains(temputxo))){
					return false;
				}
			}
			
			return true;
	}

	public boolean checkAllSignatures(Transaction tx){
			
			Crypto sigchecker = new Crypto();
			Transaction.Input in;
			Transaction.Output out;
			for(int i=0; i<tx.numInputs();i++) {
				in = tx.getInput(i);
				out = tx.getOutput(i);
				if (!(sigchecker.verifySignature(out.address, tx.getRawDataToSign(i), in.signature))){
					return false;
				}
			}
			return true;
	}

	public boolean checkForDuplicateUTXO(Transaction tx){

			ArrayList<Transaction.Input> allInputs = new ArrayList<Transaction.Input>(tx.getInputs());
			Set<Transaction.Input> setInputs = new HashSet<Transaction.Input>(allInputs);

			if(setInputs.size() < allInputs.size()) {
					return false;
			}
			return true;
	}
	
			

				
				
    /*
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
    
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    } */

}