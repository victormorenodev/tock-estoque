import { Title, Password, Email, Submit } from "./components/Login";

export default function Home() {
  return (
    <main>
      <div className="flex justify-center flex-col my-50 items-center min-w-[36rem] min-h-96 rounded-lg ">
        <Title/>
        <Email/>
        <Password/>
        <Submit/>
      </div>
    </main>
  );
}
