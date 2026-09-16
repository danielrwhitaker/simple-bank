import {Link} from 'react-router'

type AccountNavigationProps = {
    accountId: number
}

function AccountNavigation({accountId}: AccountNavigationProps) {
    const basePath = `/accounts/${accountId}`

    return (
        <nav aria-label="Account navigation">
            <Link to={basePath}>Overview</Link>
            <Link to={`${basePath}/deposit`}>Deposit</Link>
            <Link to={`${basePath}/withdraw`}>Withdraw</Link>
            <Link to={`${basePath}/transactions`}>Transactions</Link>
        </nav>
    )
}

export default AccountNavigation