import "../../css/PartialPollOption.css"
// this class is used when the poll hasnt ended, so there is a limited view
export default function PartialPollOption(props) {
    const { optionId, hasUserVoted, optionContent, onOptionSelect, isSelectedByUser, onlyOneOptionAllowed } = props

    // for (let i = 0; i < optionIdsVotedByUser.length; i++) {
    //     if (optionIdsVotedByUser[i] === optionId) {
    //         isSelectedByUser = true
    //         break
    //     }
    // }

    function handleClick() {
        if (!hasUserVoted) {
            // update parent state
            onOptionSelect(optionId);
        }
    }

    return (
        <div className={ isSelectedByUser ? "selectedPollOption": "unselectedPollOption"} onClick={handleClick}>
            { onlyOneOptionAllowed
                ? (
                    <>
                        <label className="optionContent" htmlFor="option">{optionContent}</label>
                        { !hasUserVoted && (<input type="radio" id="option" name="option" checked={isSelectedByUser}/>) }
                    </>
                   )


                : (
                    <>
                        <label className="optionContent" htmlFor="option">{optionContent}</label>
                        { !hasUserVoted && (<input type="checkbox" id="option" name="option" checked={isSelectedByUser}/>) }
                    </>
                  )
            }
        </div>
    )
}