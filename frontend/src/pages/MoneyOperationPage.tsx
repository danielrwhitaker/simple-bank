import {useState, type FormEvent} from 'react'
import {useNavigate} from 'react-router'
import AccountNavigation from '../components/AccountNavigation'
import {deposit, withdraw, type Account} from '../api/accounts'
import {useAccountId} from '../lib/accountRoute'

type MoneyOperationPageProps = {
    operation: 'deposit' | 'withdraw'
}

function MoneyOperationPage({operation}: MoneyOperationPageProps) {
    const {accountId, error} = useAccountId()
    const navigate = useNavigate()
    const [amount, setAmount] = useState('')
    const [account, setAccount] = useState<Account | null>(null)
    const [validationError, setValidationError] = useState<string | null>(null)
    const [requestError, setRequestError] = useState<string | null>(null)
    const [message, setMessage] = useState<string | null>(null)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const title = operation === 'deposit' ? 'Deposit money' : 'Withdraw money'

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setValidationError(null)
        setRequestError(null)
        setMessage(null)

        const numericAmount = Number(amount)
        if (!amount.trim() || !Number.isFinite(numericAmount) || numericAmount <= 0) {
            setValidationError('Enter a positive monetary amount.')
            return
        }
        if (accountId === null) return

        setIsSubmitting(true)
        try {
            const updatedAccount = operation === 'deposit'
                ? await deposit(accountId, numericAmount)
                : await withdraw(accountId, numericAmount)
            setAccount(updatedAccount)
            setMessage(`${operation === 'deposit' ? 'Deposit' : 'Withdrawal'} completed successfully.`)
            setAmount('')
        } catch (caughtError) {
            setRequestError(caughtError instanceof Error ? caughtError.message : `Unable to ${operation} money.`)
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <main className="account-page">
            <h1>{title}</h1>
            {error && <p role="alert" className="status error">{error}</p>}
            {accountId !== null && <AccountNavigation accountId={accountId} />}
            {accountId !== null && (
                <form onSubmit={handleSubmit} className="money-form" noValidate>
                    <label htmlFor="amount">Amount</label>
                    <input
                        id="amount"
                        name="amount"
                        type="number"
                        min="0.01"
                        step="0.01"
                        inputMode="decimal"
                        value={amount}
                        onChange={(event) => setAmount(event.target.value)}
                        aria-invalid={Boolean(validationError || requestError)}
                        disabled={isSubmitting}
                    />
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Submitting...' : title}
                    </button>
                </form>
            )}
            {validationError && <p role="alert" className="status error">{validationError}</p>}
            {requestError && <p role="alert" className="status error">{requestError}</p>}
            {message && (
                <div role="status" className="status success">
                    <p>{message}</p>
                    {account && <p>Updated balance: {account.balance}</p>}
                    <button type="button" onClick={() => navigate(`/accounts/${accountId}`)}>Back to account</button>
                </div>
            )}
        </main>
    )
}

export default MoneyOperationPage