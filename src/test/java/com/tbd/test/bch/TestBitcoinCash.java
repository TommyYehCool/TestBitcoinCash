package com.tbd.test.bch;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.junit.Test;

/**
 * <pre>
 * 測試 BCH
 * 
 * https://www.oodlestechnologies.com/blogs/How-to-Communicate-with-Bitcoin-Cash-Blockchain-Using-Bitcoinj
 * 
 * https://mvnrepository.com/artifact/cash.bitcoinj/bitcoinj-core
 * </pre>
 * 
 * @author tommy.feng
 *
 */
public class TestBitcoinCash {

	@Test
	public void testCreateWallet() throws BlockStoreException {
		final NetworkParameters params = TestNet3Params.get();
		Wallet wallet = new Wallet(params);

		System.out.println(wallet.toString());

		BlockStore blockStore = new MemoryBlockStore(params);
		BlockChain chain = new BlockChain(params, wallet, blockStore);

		final PeerGroup peerGroup = new PeerGroup(params, chain);
		peerGroup.addWallet(wallet);

		peerGroup.startAsync();

		wallet.addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
			public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
				System.out.println("\nReceived tx " + tx.getHashAsString());
				System.out.println(tx.toString());
			}
		});

		// Now download and process the block chain.
		peerGroup.downloadBlockChain();
		peerGroup.stopAsync();
		
		System.out.println("\nDone!\n");
		System.out.println(wallet.toString());
	}

}
