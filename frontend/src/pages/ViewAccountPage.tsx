import {useCallback, useEffect, useState} from 'react'
import AccountNavigation from '../components/AccountNavigation'
import {getAccount, type Account} from '../api/accounts'
import {useAccountId} from '../lib/accountRoute'

function ViewAccountPage() {
    const {accountId, error} = useAccountId()
    const [account, setAccount] = useState<Account | null>(null)
    const [loadError, setLoadError] = useState<string | null>(null)
    const [isLoading, setIsLoading] = useState(false)

    const loadAccount = useCallback(async () => {
        if (accountId === null) return
        setIsLoading(true)
        setLoadError(null)
        try {
            setAccount(await getAccount(accountId))
        } catch (caughtError) {
            setLoadError(caughtError instanceof Error ? caughtError.message : 'Unable to load account.')
        } finally {
            setIsLoading(false)
        }
    }, [accountId])

    useEffect(() => {
        void loadAccount()
    }, [loadAccount])

    return (
        <main className="account-page">
            <h1>View Account</h1>
            {error && <p role="alert" className="status error">{error}</p>}
            {accountId !== null && <AccountNavigation accountId={accountId} />}
            {isLoading && <p role="status">Loading account...</p>}
            {loadError && (
                <div className="status error" role="alert">
                    <p>{loadError}</p>
                    <button type="button" onClick={() => void loadAccount()}>Try again</button>
                </div>
            )}
            {account && !isLoading && !loadError && (
                <section className="account-summary" aria-labelledby="account-summary-heading">
                    <h2 id="account-summary-heading">Account summary</h2>
                    <dl>
                        <div><dt>Account ID</dt><dd>{account.id}</dd></div>
                        <div><dt>Type</dt><dd>{account.type}</dd></div>
                        <div><dt>Balance</dt><dd>{account.balance}</dd></div>
                    </dl>
                </section>
            )}
        </main>
    )
}

export default ViewAccountPage