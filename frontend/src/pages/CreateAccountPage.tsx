import { useState, type SyntheticEvent } from 'react'
import { Link } from 'react-router'

const API_URL = import.meta.env.VITE_API_URL

function CreateAccountPage() {
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [type, setType] = useState('')
    const [balance, setBalance] = useState('')
    const [error, setError] = useState('')
    const [message, setMessage] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    async function handleSubmit(event: SyntheticEvent<HTMLFormElement>) {
        event.preventDefault()
        setError('')
        setMessage('')
        setIsSubmitting(true)

        try {
            const userResponse = await fetch(`${API_URL}/api/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, email, password })
            })

            if (!userResponse.ok) {
                throw new Error(`Unable to create user (${userResponse.status})`)
            }

            const user = await userResponse.json() as { id: number }
            const loginResponse = await fetch(`${API_URL}/api/users/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            })

            if (!loginResponse.ok) {
                throw new Error(`Unable to sign in (${loginResponse.status})`)
            }

            const { token } = await loginResponse.json() as { token: string }
            localStorage.setItem('token', token)

            const accountResponse = await fetch(`${API_URL}/api/accounts`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({
                    userId: user.id,
                    accountType: type,
                    balance: Number(balance)
                })
            })

            if (!accountResponse.ok) {
                throw new Error(`Unable to create account (${accountResponse.status})`)
            }

            const account = await accountResponse.json() as { id: number }
            setMessage(`Account ${account.id} created successfully.`)
        } catch (caughtError) {
            setError(caughtError instanceof Error ? caughtError.message : 'An unexpected error occurred.')
        } finally {
            setIsSubmitting(false)
        }
    }


    return (
        <main className="account-page">
            <h1>Create Account</h1>
            <Link to="/">Home</Link>

            <form className="money-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="name">Name</label>
                    <input
                        id="name"
                        type="text"
                        value={name}
                        onChange={(event) => setName(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        autoComplete="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        minLength={8}
                        autoComplete="new-password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="account-type">Account Type</label>
                    <select
                        id="account-type"
                        value={type}
                        onChange={(event) => setType(event.target.value)}
                        required
                    >
                        <option value="">Select account type</option>
                        <option value="CHECKING">Checking</option>
                        <option value="SAVINGS">Savings</option>
                    </select>
                </div>

                <div>
                    <label htmlFor="balance">Balance</label>
                    <input
                        id="balance"
                        type="number"
                        min="0"
                        step="0.01"
                        value={balance}
                        onChange={(event) => setBalance(event.target.value)}
                        required
                    />
                </div>

                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Creating...' : 'Submit'}
                </button>
            </form>

            {error && <p className="status error" role="alert">{error}</p>}
            {message && <p className="status success" role="status">{message}</p>}
        </main>
    )
}

export default CreateAccountPage
