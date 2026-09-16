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
        <div>
            <h1>Simple Bank</h1>
            <Link to="/create-account">
                <button>Create Account</button>
            </Link>
            <form onSubmit={handleSubmit}>
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
        </div>
    )
}

export default HomePage