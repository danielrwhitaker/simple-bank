import {useState, type FormEvent} from 'react'
import {Link, useNavigate} from 'react-router'

function HomePage() {
    const [accountId, setAccountId] = useState('')
    const navigate = useNavigate()

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (/^\d+$/.test(accountId) && Number(accountId) > 0) {
            navigate(`/accounts/${accountId}`)
        }
    }

    return (
        <main className="account-page home-page">
            <h1>Simple Bank</h1>
            <p>Create a new bank account or open an existing account by ID.</p>
            <Link className="button-link" to="/create-account">Create Account</Link>
            <form className="money-form" onSubmit={handleSubmit}>
                <label htmlFor="account-id">View account</label>
                <input
                    id="account-id"
                    name="accountId"
                    inputMode="numeric"
                    value={accountId}
                    onChange={(event) => setAccountId(event.target.value)}
                    placeholder="Account ID"
                    required
                />
                <button type="submit">View Account</button>
            </form>
        </main>
    )
}

export default HomePage
