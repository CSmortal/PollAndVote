import React, {useState} from "react";
import "../../css/CreatePollOption.css"

const CreatePollOption = React.forwardRef((props, ref) => {
    const [ optionContent, setOptionContent ] = useState("")

    function handleChange(e) {
        setOptionContent(e.target.value)
        // console.log(e.target.value)
    }

    return (
        <div className="createPollOption">
            <input className="createPollOption-input"
                type="text"
                value={optionContent}
                onChange={handleChange}
                placeholder="Enter your option..."
                ref={ref}
            />
        </div>
    )
})
export default CreatePollOption;