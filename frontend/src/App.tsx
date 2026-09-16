import { Routes, Route } from 'react-router'
import HomePage from './pages/HomePage'
import CreateAccountPage from './pages/CreateAccountPage'
import ViewAccountPage from './pages/ViewAccountPage'
import DepositPage from './pages/DepositPage'
import WithdrawPage from './pages/WithdrawPage'
import TransactionHistoryPage from './pages/TransactionHistoryPage'

function App() {
    return (
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/create-account" element={<CreateAccountPage />} />
            <Route path="/accounts/:accountId" element={<ViewAccountPage />} />
            <Route path="/accounts/:accountId/deposit" element={<DepositPage />} />
            <Route path="/accounts/:accountId/withdraw" element={<WithdrawPage />} />
            <Route path="/accounts/:accountId/transactions" element={<TransactionHistoryPage />} />
        </Routes>
    )
}

export default App