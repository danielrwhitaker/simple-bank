import { useState, type SyntheticEvent } from 'react'

function CreateAccountPage() {
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
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
            const userResponse = await fetch('http://localhost:8080/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, email })
            })

            if (!userResponse.ok) {
                throw new Error(`Unable to create user (${userResponse.status})`)
            }

            const user = await userResponse.json() as { id: number }
            const accountResponse = await fetch(`http://localhost:8080/api/users/${user.id}/accounts`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    type,
                    balance: Number(balance)
                })
            })

            if (!accountResponse.ok) {
                throw new Error(`Unable to create account (${accountResponse.status})`)
            }

            setMessage('Account created successfully.')
        } catch (caughtError) {
            setError(caughtError instanceof Error ? caughtError.message : 'An unexpected error occurred.')
        } finally {
            setIsSubmitting(false)
        }
    }


    return (
        <div>
            <h1>Create Account</h1>

            <form onSubmit={handleSubmit}>
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
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
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
                        value={balance}
                        onChange={(event) => setBalance(event.target.value)}
                        required
                    />
                </div>

                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Creating...' : 'Submit'}
                </button>
            </form>

            {error && <p role="alert">{error}</p>}
            {message && <p role="status">{message}</p>}
        </div>
    )
}

export default CreateAccountPage