import "../../css/FullPollOption.css"

export default function FullPollOption(props) {
    // FullPollOption rendered only if user has voted or poll has already ended
    const { optionContent, isSelectedByUser, percentageVoted } = props

    // eventually, we want to make each full option show a bar that is filled based on the percentage voted for this option
    return (
        <div className={ isSelectedByUser ? "selectedPollOptionFull" : "unselectedPollOptionFull" }>
            <p className="optionContent">{optionContent}</p>
            <p className="percentageVoted">{percentageVoted+ "%"}</p>
        </div>
    )
}