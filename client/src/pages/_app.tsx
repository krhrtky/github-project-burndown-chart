import '../../styles/globals.css'
import {AuthProvider} from "../libs/firebase/auth";

const MyApp = ({ Component, pageProps }) => (
        <AuthProvider>
            <Component {...pageProps} />
        </AuthProvider>
)

export default MyApp
