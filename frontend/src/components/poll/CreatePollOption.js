import React, {useState} from "react";

const CreatePollOption = React.forwardRef((props, ref) => {
    const [ optionContent, setOptionContent ] = useState("")

    function handleChange(e) {
        setOptionContent(e.target.value)
        // console.log(e.target.value)
    }

    return (
        <div>
            <input
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