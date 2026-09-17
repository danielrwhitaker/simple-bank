import {useCallback, useEffect, useState} from 'react'
import AccountNavigation from '../components/AccountNavigation'
import {getTransactions, type Transaction} from '../api/accounts'
import {useAccountId} from '../lib/accountRoute'

function formatTimestamp(timestamp: string): string {
    const date = new Date(timestamp)
    return Number.isNaN(date.getTime()) ? timestamp : date.toLocaleString()
}

function TransactionHistoryPage() {
    const {accountId, error} = useAccountId()
    const [transactions, setTransactions] = useState<Transaction[]>([])
    const [isLoading, setIsLoading] = useState(false)
    const [loadError, setLoadError] = useState<string | null>(null)

    const loadTransactions = useCallback(async () => {
        if (accountId === null) return
        setIsLoading(true)
        setLoadError(null)
        try {
            const result = await getTransactions(accountId)
            setTransactions([...result].sort((left, right) => (
                new Date(right.timestamp).getTime() - new Date(left.timestamp).getTime()
            )))
        } catch (caughtError) {
            setLoadError(caughtError instanceof Error ? caughtError.message : 'Unable to load transactions.')
        } finally {
            setIsLoading(false)
        }
    }, [accountId])

    useEffect(() => {
        void loadTransactions()
    }, [loadTransactions])

    return (
        <main className="account-page">
            <h1>Transaction History</h1>
            {error && <p role="alert" className="status error">{error}</p>}
            {accountId !== null && <AccountNavigation accountId={accountId} />}
            {isLoading && <p role="status">Loading transactions...</p>}
            {loadError && (
                <div role="alert" className="status error">
                    <p>{loadError}</p>
                    <button type="button" onClick={() => void loadTransactions()}>Try again</button>
                </div>
            )}
            {!isLoading && !loadError && transactions.length === 0 && (
                <p role="status" className="status">No transactions for this account yet.</p>
            )}
            {!isLoading && !loadError && transactions.length > 0 && (
                <div className="transaction-table-wrapper">
                    <table className="transaction-table">
                        <caption>Transactions, newest first</caption>
                        <thead>
                            <tr>
                                <th scope="col">Transaction ID</th>
                                <th scope="col">Type</th>
                                <th scope="col">Amount</th>
                                <th scope="col">Timestamp</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.id}>
                                    <td>{transaction.id}</td>
                                    <td>{transaction.type}</td>
                                    <td>{transaction.amount}</td>
                                    <td>{formatTimestamp(transaction.timestamp)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </main>
    )
}

export default TransactionHistoryPage
