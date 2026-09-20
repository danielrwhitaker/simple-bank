import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'

const API_URL = import.meta.env.VITE_API_URL

type LoginResponse = {
    token: string
}

function LoginPage() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)
    const navigate = useNavigate()

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setError('')
        setIsSubmitting(true)

        try {
            const response = await fetch(`${API_URL}/api/users/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            })

            if (!response.ok) {
                throw new Error(response.status === 401
                    ? 'Invalid email or password.'
                    : 'Unable to sign in.')
            }

            const { token } = await response.json() as LoginResponse
            localStorage.setItem('token', token)
            navigate('/', { replace: true })
        } catch (caughtError) {
            setError(caughtError instanceof Error ? caughtError.message : 'Unable to sign in.')
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <main className="account-page">
            <h1>Sign In</h1>
            <Link to="/">Home</Link>
            <form className="money-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        autoComplete="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        disabled={isSubmitting}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        disabled={isSubmitting}
                        required
                    />
                </div>
                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Signing in...' : 'Sign In'}
                </button>
            </form>
            {error && <p className="status error" role="alert">{error}</p>}
        </main>
    )
}

export default LoginPage
