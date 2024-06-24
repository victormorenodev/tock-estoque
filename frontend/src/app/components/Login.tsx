export function Title() {
    return (
        <div className="text-2xl font-bold pb-2">TockEstoque Login</div>
    )
}

export function Submit() {
    return (
        <>
            <button type="submit" className="bg-[#008ef0] my-4 h-10 w-48 rounded">Sign In</button>
        </>
    )
}

export function Password() {
    return (
        <>
            <input placeholder="Password" type="password" autoComplete="on" className="rounded bg-[#d7dee5] my-2 h-10 w-48"/>
        </>
    )
}

export function Email() {
    return (
        <>
            <input placeholder="Email" type="email" className="rounded bg-[#d7dee5] my-2 h-10 w-48"/>
        </>
    )
}